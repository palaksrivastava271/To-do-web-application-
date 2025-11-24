import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToDoListWebApp {

    // Simple Task class
    static class Task {
        String description;
        boolean isCompleted;

        public Task(String description) {
            this.description = description;
            this.isCompleted = false;
        }
    }

    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Define contexts (routes)
        server.createContext("/", new RootHandler());
        server.createContext("/add", new AddHandler());
        server.createContext("/remove", new RemoveHandler());
        server.createContext("/complete", new CompleteHandler());

        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + port);
        System.out.println("Go to http://localhost:" + port + " in your browser.");
        server.start();
    }

    // Handler for the main page (GET /)
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>");
            html.append("<html lang='en'>");
            html.append("<head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<title>My To-Do List</title>");
            html.append("<style>");
            html.append(
                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f9; color: #333; display: flex; justify-content: center; padding-top: 50px; }");
            html.append(
                    ".container { background: white; padding: 2rem; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: 400px; }");
            html.append("h1 { text-align: center; color: #4a90e2; }");
            html.append("ul { list-style-type: none; padding: 0; }");
            html.append(
                    "li { background: #fff; border-bottom: 1px solid #eee; padding: 10px; display: flex; justify-content: space-between; align-items: center; }");
            html.append("li.completed span { text-decoration: line-through; color: #888; }");
            html.append(".actions { display: flex; gap: 5px; }");
            html.append(
                    "button { cursor: pointer; border: none; padding: 5px 10px; border-radius: 4px; font-size: 0.8rem; }");
            html.append(".btn-complete { background-color: #2ecc71; color: white; }");
            html.append(".btn-remove { background-color: #e74c3c; color: white; }");
            html.append(".add-form { display: flex; gap: 10px; margin-top: 20px; }");
            html.append(
                    "input[type='text'] { flex-grow: 1; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }");
            html.append(".btn-add { background-color: #4a90e2; color: white; padding: 8px 15px; font-size: 1rem; }");
            html.append("</style>");
            html.append("</head>");
            html.append("<body>");
            html.append("<div class='container'>");
            html.append("<h1>To-Do List</h1>");

            html.append("<ul>");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                html.append("<li class='" + (task.isCompleted ? "completed" : "") + "'>");
                html.append("<span>" + escapeHtml(task.description) + "</span>");
                html.append("<div class='actions'>");

                if (!task.isCompleted) {
                    html.append("<form action='/complete' method='post' style='display:inline;'>");
                    html.append("<input type='hidden' name='index' value='" + i + "'>");
                    html.append("<button type='submit' class='btn-complete'>&#10003;</button>");
                    html.append("</form>");
                }

                html.append("<form action='/remove' method='post' style='display:inline;'>");
                html.append("<input type='hidden' name='index' value='" + i + "'>");
                html.append("<button type='submit' class='btn-remove'>&#10005;</button>");
                html.append("</form>");

                html.append("</div>");
                html.append("</li>");
            }
            html.append("</ul>");

            html.append("<form class='add-form' action='/add' method='post'>");
            html.append("<input type='text' name='description' placeholder='New task...' required>");
            html.append("<button type='submit' class='btn-add'>Add</button>");
            html.append("</form>");

            html.append("</div>");
            html.append("</body>");
            html.append("</html>");

            String response = html.toString();
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Handler to add a task (POST /add)
    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                Map<String, String> params = parseFormData(t);
                String description = params.get("description");
                if (description != null && !description.trim().isEmpty()) {
                    tasks.add(new Task(description));
                }
            }
            redirectHome(t);
        }
    }

    // Handler to remove a task (POST /remove)
    static class RemoveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                Map<String, String> params = parseFormData(t);
                try {
                    int index = Integer.parseInt(params.get("index"));
                    if (index >= 0 && index < tasks.size()) {
                        tasks.remove(index);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid index
                }
            }
            redirectHome(t);
        }
    }

    // Handler to mark a task as complete (POST /complete)
    static class CompleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                Map<String, String> params = parseFormData(t);
                try {
                    int index = Integer.parseInt(params.get("index"));
                    if (index >= 0 && index < tasks.size()) {
                        tasks.get(index).isCompleted = true;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid index
                }
            }
            redirectHome(t);
        }
    }

    // Helper to redirect back to the root page
    private static void redirectHome(HttpExchange t) throws IOException {
        t.getResponseHeaders().set("Location", "/");
        t.sendResponseHeaders(302, -1);
        t.close();
    }

    // Helper to parse form data
    private static Map<String, String> parseFormData(HttpExchange t) throws IOException {
        Map<String, String> params = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    params.put(key, value);
                }
            }
        }
        return params;
    }

    // Helper to escape HTML characters
    private static String escapeHtml(String s) {
        if (s == null)
            return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
