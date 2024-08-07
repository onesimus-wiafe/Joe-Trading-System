using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using OpenTelemetry.Metrics;
using OpenTelemetry.Resources;
using Microsoft.IdentityModel.Logging;

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

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
  .AddJwtBearer(o =>
  {
    var jwtSettings = builder.Configuration.GetSection("Jwt");
    var secretKey = jwtSettings["Secret"];
    var issuer = jwtSettings["Issuer"];
    var audience = jwtSettings["Audience"];
    
    o.TokenValidationParameters = new TokenValidationParameters
    {
      ValidateIssuer = true,
      ValidIssuer = issuer,
      ValidateAudience = true,
      ValidAudiences = [audience],
      ValidateLifetime = true,
      ValidateIssuerSigningKey = true,
      IssuerSigningKey = new SymmetricSecurityKey(
        Convert.FromBase64String(secretKey)
      ),
      RequireSignedTokens = true,
      RequireExpirationTime = true,
      ValidateTokenReplay = true,
      TryAllIssuerSigningKeys = true,
      ClockSkew = TimeSpan.Zero
    };
  });

var app = builder.Build();

app.UseOpenTelemetryPrometheusScrapingEndpoint();
app.UseAuthentication();
app.UseAuthorization();

IdentityModelEventSource.ShowPII = true;

app.UseCors("AllowAll");

app.MapReverseProxy();

app.Run();
