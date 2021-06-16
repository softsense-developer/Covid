using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Swashbuckle.AspNetCore.Swagger;
using Swashbuckle.AspNetCore.SwaggerUI;
using System;
using System.Collections.Generic;
using Microsoft.OpenApi.Models;

namespace Smartsense.Api.Extensions
{
    public static class SwaggerServiceExtensions
    {
        private static string SmartsenseApiVersion = "v1";
        private static string SmartsenseApiName = "Smartsense API";
        private static string SmartsenseApiDesc = "Welcome to Smartsense API";

        public static IServiceCollection AddSwaggerDocumentation(this IServiceCollection services)
        {
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc(SmartsenseApiVersion, new OpenApiInfo
                {
                    Version = SmartsenseApiVersion,
                    Title = SmartsenseApiName,
                    Description = SmartsenseApiDesc
                });

                c.EnableAnnotations();
                
                // Authorize => Bearer <token>  
                c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
                {
                    Description = @"JWT Authorization header using the Bearer scheme. Example: Authorization: Bearer {token}",
                    Name = "Authorization",
                    In = ParameterLocation.Header,
                    Type = SecuritySchemeType.ApiKey,
                    Scheme = "Bearer"
                });

                c.AddSecurityRequirement(new OpenApiSecurityRequirement
                {
                    {
                        new OpenApiSecurityScheme
                        {
                            Reference = new OpenApiReference
                            {
                                Type = ReferenceType.SecurityScheme,
                                Id = "Bearer"
                            }
                        },
                    Array.Empty<string>()
                    }
                });
            });



            return services;
        }

        public static IApplicationBuilder UseSwaggerDocumentation(this IApplicationBuilder app)
        {
            app.UseDeveloperExceptionPage();
            app.UseSwagger();
            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", SmartsenseApiName);
                c.DocumentTitle = SmartsenseApiDesc;
                c.DocExpansion(DocExpansion.None);
            });
            return app;
        }
    }
}
