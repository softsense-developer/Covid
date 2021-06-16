using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Models;


namespace Smartsense.Data.Concrete.Ef
{
    public class SmartsenseDbContext : DbContext
    {
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseSqlServer("Data Source='95.70.241.241,1433\\MSSQLSERVER'; Initial Catalog=SmartsenseDB; user id=sera;password=Sagtek21;MultipleActiveResultSets=True;");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

            modelBuilder
                .Entity<Patient>()
                .Property(e => e.UserStatus)
                .HasConversion<string>();

            modelBuilder
                .Entity<Patient>()
                .Property(e => e.Gender)
                .HasConversion<string>();

            

            modelBuilder
                .Entity<Value>()
                .Property(e => e.DataType)
                .HasConversion<string>();
            modelBuilder
                .Entity<Warning>()
                .Property(e => e.WarningData)
                .HasConversion<string>();
        }


        public DbSet<User> Users { get; set; }

        public DbSet<Admin> Admins { get; set; }

        public DbSet<MailVerify> MailVerifies { get; set; }

        public DbSet<Location> Locations { get; set; }

        public DbSet<LocationWarning> LocationWarnings { get; set; }

        public DbSet<Supervisor> Supervisors { get; set; }

        public DbSet<Viewer> Viewers { get; set; }

        public DbSet<Value> Values{ get; set; }

        public DbSet<Warning> Warnings { get; set; }

        public DbSet<Patient> Patients { get; set; }

        public DbSet<Role> Roles { get; set; }

        public DbSet<UserRole> UserRoles { get; set; }

        public DbSet<Doctor> Doctors { get; set; }

        public DbSet<Connection> Requests { get; set; }

        public DbSet<PasswordReset> PasswordResets { get; set; }

        public DbSet<Hospital> Hospitals { get; set; }

        public DbSet<Companion> Companions { get; set; }
    }
}
