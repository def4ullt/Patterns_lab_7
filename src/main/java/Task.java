import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.*;

public class Task {
    private int id;
    private String name;
    private String text;
    private LocalDateTime endTime;

    public Task(int id, String name, String text, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("text", text);
        jsonObject.addProperty("endTime", endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return jsonObject;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedEndTime = (endTime != null) ? endTime.format(formatter) : "N/A";
        return "Task{name='" + name + "', text='" + text + "', endTime=" + formattedEndTime + "}";
    }
}