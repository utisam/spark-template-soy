# spark-template-soy

How to use the Google Closure Templates (Soy) route for Spark example:

```java
public static void main(final String[] args) {
    String templateDirectory = args[0];

    // Use StandardSoyTofuStore if you do not want to enable auto reloading
    SoyTofuStore tofuStore = new AutoReloadSoyTofuStore(templateDirectory);
    SoyTemplateEngine templateEngine = new SoyTemplateEngine();

    get("/", (req, res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "Soy World");
        model.put("person", "Foobar");

        return tofuStore.getTofu().newRenderer("com.example.page.index")
            .setData(model);
    }, templateEngine);
}
```
