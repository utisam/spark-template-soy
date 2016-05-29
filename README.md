# spark-template-soy

How to use the Google Soy template route for Spark example:

```java
public static void main(final String[] args) {
    templateDirectory = args[1];

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
