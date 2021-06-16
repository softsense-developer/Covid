using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Cors;
using Microsoft.Extensions.DependencyInjection;

namespace Smartsense.Api.Extensions
{
    public static class CorsServiceExtensions
    {
        private static string SmartsenseUiOrigins = "_smartsenseUiOrigins";

        public static IServiceCollection AddCorsForSmartsense(this IServiceCollection services)
        {
            services.AddCors(options =>
            {
                options.AddPolicy(SmartsenseUiOrigins,
                builder =>
                {
                    builder
                        .AllowAnyOrigin()
                        .AllowAnyMethod()
                        .AllowAnyHeader();
                });
            });
            
            /*services.Configure<MvcOptions>(options =>
            {
                options.Filters.Add(new CorsAuthorizationFilterFactory(SmartsenseUiOrigins));
            });*/

            return services;
        }

        public static IApplicationBuilder UseCorsForSmartsense(this IApplicationBuilder app)
        {
            app.UseCors(SmartsenseUiOrigins);
            return app;
        }
    }
}
