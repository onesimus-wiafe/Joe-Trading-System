using OpenTelemetry.Metrics;
using OpenTelemetry.Resources;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddReverseProxy()
  .LoadFromConfig(builder.Configuration.GetSection("ReverseProxy"));
builder.Services.AddOpenTelemetry()
  .ConfigureResource(resource => resource.AddService(serviceName: builder.Environment.ApplicationName))
  .WithMetrics(metrics =>
  {
    metrics
      .AddAspNetCoreInstrumentation()
      .AddRuntimeInstrumentation()
      .AddPrometheusExporter();
  });

builder.Services.AddCors(options =>
{
  options.AddPolicy("AllowAll", builder => builder.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());
});

var app = builder.Build();

app.UseOpenTelemetryPrometheusScrapingEndpoint();
app.MapReverseProxy();
app.UseCors("AllowAll");

app.Run();
