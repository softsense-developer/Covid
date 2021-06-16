using FluentValidation;
using FluentValidation.AspNetCore;
using Smartsense.Data.Abstract;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Http;
using Smartsense.Business.Abstract;
using Smartsense.Business.Concrete;
using Smartsense.Data.Concrete.Ef;

namespace Smartsense.Api.Extensions
{
    public static class DependencyInjectionServiceExtensions
    {
        public static IServiceCollection AddDependency(this IServiceCollection services)
        {
            services.AddControllersWithViews()
                .AddFluentValidation();

            services.AddScoped<IUserService, UserService>();
            services.AddScoped<IValueService, ValueService>();
            services.AddScoped<IPatientService, PatientService>();
            services.AddScoped<IDoctorService, DoctorService>();
            services.AddScoped<ISupervisorService, SupervisorService>();
            services.AddScoped<IAdminService, AdminService>();
            services.AddScoped<ICompanionService, CompanionService>();

            services.AddScoped<IUserRepository, EfUserRepository>();
            services.AddScoped<IMailVerifyRepository, EfMailVerifyRepository>();
            services.AddScoped<IValueRepository, EfVAlueRepository>();
            services.AddScoped<IWarningRepository, EfWarningRepository>();
            services.AddScoped<IPatientRepository, EfPatientRepository>();
            services.AddScoped<IRoleRepository, EfRoleRepository>();
            services.AddScoped<IUserRoleRepository, EfUserRoleRepository>();
            services.AddScoped<IDoctorRepository, EfDoctorRepository>();
            services.AddScoped<ISupervisorRepository, EfSupervisorRepository>();
            services.AddScoped<IConnectionRepository, EfConnectionRepository>();
            services.AddScoped<ILocationRepository, EfLocationRepository>();
            services.AddScoped<ILocationWarningRepository, EfLocationWarningRepository>();
            services.AddScoped<IPasswordResetRepository, EfPasswordResetRepository>();
            services.AddScoped<IHospitalRepository, EfHospitalRepository>();
            services.AddScoped<IAdminRepository, EfAdminRepository>();
            services.AddScoped<ICompanionRepository, EfCompanionRepository>();

            return services;
        }
    }
}
