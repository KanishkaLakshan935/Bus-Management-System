import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import filehandling.ConductorFileHandler;
import model.Conductor;
import model.Ticket;
import model.LostItemReport;
import service.ConductorService;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;

public class ConductorWeb {
    private final Conductor profile = new Conductor(
            845, "Gimhan", "gimhan@example.com", "demo123", "0771234567", "CON001", 40);
    private final ConductorFileHandler files = new ConductorFileHandler("data/conductor_845.properties");
    private final Set<String> sessions = new HashSet<>();
    private ConductorService service;
    private boolean storageAvailable = true;

    public static void main(String[] args) throws IOException {
        new ConductorWeb().start();
    }

    private void start() throws IOException {
        if (!Files.isRegularFile(Path.of("web/conductor.html"))) {
            throw new IOException("web/conductor.html is missing. Set Working directory to the project folder.");
        }
        service = files.load(profile);
        files.save(service);
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8091), 0);
        server.createContext("/", this::handle);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
        System.out.println("Conductor interface: http://127.0.0.1:8091");
        System.out.println("Keep this running. Do not run ConductorApp at the same time.");
    }

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            if ((path.equals("/") || path.equals("/conductor.html")) && method.equals("GET")) {
                send(exchange, 200, Files.readString(Path.of("web/conductor.html")), "text/html");
                return;
            }
            if (!path.startsWith("/api/")) {
                send(exchange, 404, "Not found", "text/plain");
                return;
            }
            String origin = exchange.getRequestHeaders().getFirst("Origin");
            if (origin != null && !origin.equals("http://127.0.0.1:8091")
                    && !origin.equals("http://localhost:8091")) {
                json(exchange, 403, "{\"error\":\"Invalid origin.\"}");
                return;
            }
            if (!method.equals("GET") && !method.equals("POST")) {
                json(exchange, 405, "{\"error\":\"Method not allowed.\"}");
                return;
            }
            Map<String, String> input = method.equals("POST") ? readInput(exchange) : new HashMap<>();
            if (path.equals("/api/login") && method.equals("POST")) {
                if (!profile.getEmail().equalsIgnoreCase(required(input, "email"))
                        || !profile.login(required(input, "password"))) {
                    json(exchange, 401, "{\"error\":\"Incorrect email or password.\"}");
                    return;
                }
                String token = UUID.randomUUID().toString();
                sessions.add(token);
                json(exchange, 200, "{\"token\":" + quote(token) + "}");
                return;
            }
            String authorization = exchange.getRequestHeaders().getFirst("Authorization");
            String token = authorization != null && authorization.startsWith("Bearer ")
                    ? authorization.substring(7) : "";
            if (!sessions.contains(token)) {
                json(exchange, 401, "{\"error\":\"Please sign in.\"}");
                return;
            }
            if (path.equals("/api/logout") && method.equals("POST")) {
                sessions.remove(token);
                json(exchange, 200, "{\"message\":\"Signed out.\"}");
            } else if (path.equals("/api/state") && method.equals("GET")) {
                json(exchange, 200, state());
            } else if (path.equals("/api/action") && method.equals("POST")) {
                action(exchange, input);
            } else {
                json(exchange, 404, "{\"error\":\"Not found.\"}");
            }
        } catch (NumberFormatException e) {
            json(exchange, 400, "{\"error\":\"Enter a valid number. Fares must be whole rupees.\"}");
        } catch (IllegalArgumentException e) {
            json(exchange, 400, "{\"error\":" + quote(e.getMessage()) + "}");
        } catch (IOException e) {
            e.printStackTrace();
            json(exchange, 500, "{\"error\":\"File operation failed. Check the Java console.\"}");
        } finally {
            exchange.close();
        }
    }

    private void action(HttpExchange exchange, Map<String, String> input) throws IOException {
        if (!storageAvailable) {
            json(exchange, 503, "{\"error\":\"Storage is unavailable. Restart after fixing the file error.\"}");
            return;
        }
        String action = required(input, "action");
        if (action.equals("commission")) {
            double amount = service.calculateCommission(Double.parseDouble(required(input, "rate")));
            json(exchange, 200, "{\"amount\":" + amount + "}");
            return;
        }
        String message;
        switch (action) {
            case "board":
                service.recordBoarding(required(input, "passengerId"));
                message = "Passenger boarded.";
                break;
            case "exit":
                service.recordExiting(required(input, "passengerId"));
                message = "Passenger exit recorded.";
                break;
            case "ticket":
                Ticket ticket = service.issueTicket(required(input, "passengerId"),
                        Integer.parseInt(required(input, "fare")));
                message = "Ticket " + ticket.getTicketId() + " issued.";
                break;
            case "lost":
                LostItemReport report = service.reportLostItem(required(input, "passengerId"),
                        required(input, "description"));
                message = "Report " + report.getReportId() + " created.";
                break;
            case "status":
                service.updateLostItemStatus(Integer.parseInt(required(input, "reportId")),
                        required(input, "status"));
                message = "Report status updated.";
                break;
            case "save":
                message = "Records saved.";
                break;
            default:
                throw new IllegalArgumentException("Unknown action.");
        }
        try {
            files.save(service);
        } catch (IOException failure) {
            try {
                service = files.load(new Conductor(845, "Gimhan", "gimhan@example.com",
                        "demo123", "0771234567", "CON001", 40));
            } catch (IOException reloadFailure) {
                storageAvailable = false;
            }
            throw failure;
        }
        json(exchange, 200, "{\"message\":" + quote(message) + "}");
    }

    private String state() {
        List<String> passengers = new ArrayList<>();
        List<String> without = service.getPassengersWithoutTickets();
        for (String id : service.getConductor().getOnboardPassengerIds()) {
            passengers.add("{\"id\":" + quote(id) + ",\"hasTicket\":" + !without.contains(id) + "}");
        }
        List<String> tickets = new ArrayList<>();
        for (Ticket ticket : service.getTickets()) {
            tickets.add("{\"id\":" + ticket.getTicketId() + ",\"passengerId\":"
                    + quote(ticket.getPassengerId()) + ",\"fare\":" + ticket.getFare()
                    + ",\"active\":" + ticket.isActive() + "}");
        }
        List<String> reports = new ArrayList<>();
        for (LostItemReport report : service.getLostItemReports()) {
            reports.add("{\"id\":" + report.getReportId() + ",\"passengerId\":"
                    + quote(report.getPassengerId()) + ",\"description\":" + quote(report.getDescription())
                    + ",\"status\":" + quote(report.getStatus()) + "}");
        }
        return "{\"name\":" + quote(profile.getName())
                + ",\"count\":" + service.getPassengerCount()
                + ",\"seats\":" + service.getAvailableSeats()
                + ",\"capacity\":" + service.getConductor().getSeatCapacity()
                + ",\"revenue\":" + service.getTotalRevenue()
                + ",\"ticketCount\":" + service.getTicketCount()
                + ",\"unticketed\":" + without.size()
                + ",\"passengers\":[" + String.join(",", passengers)
                + "],\"tickets\":[" + String.join(",", tickets)
                + "],\"reports\":[" + String.join(",", reports) + "]}";
    }

    private Map<String, String> readInput(HttpExchange exchange) throws IOException {
        byte[] bytes = exchange.getRequestBody().readNBytes(16385);
        if (bytes.length > 16384) throw new IllegalArgumentException("Input is too long.");
        Map<String, String> input = new HashMap<>();
        for (String pair : new String(bytes, StandardCharsets.UTF_8).split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                input.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }
        return input;
    }

    private String required(Map<String, String> input, String key) {
        String value = input.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter " + key + ".");
        }
        return value.trim();
    }

    private String quote(String value) {
        StringBuilder result = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            if (c == '"' || c == '\\') result.append('\\').append(c);
            else if (c < 32) result.append(String.format("\\u%04x", (int) c));
            else result.append(c);
        }
        return result.append('"').toString();
    }

    private void json(HttpExchange exchange, int status, String body) throws IOException {
        send(exchange, status, body, "application/json");
    }

    private void send(HttpExchange exchange, int status, String body, String type) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type + "; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
    }
}
