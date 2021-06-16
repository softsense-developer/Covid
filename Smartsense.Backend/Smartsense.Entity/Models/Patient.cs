using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.Models
{
    [Table("patients")]
    public class Patient :BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("doctor_id")]
        public long DoctorId { get; set; }

        [Column("identity_number")]
        public string IdentityNumber { get; set; }

        [Column("gender")]
        public GenderStatus Gender { get; set; }

        [Column("date_of_birth")]
        public DateTime DateOfBirth { get; set; }

        [Column("user_status")]
        public UserStatus UserStatus { get; set; }

        [Column("diagnosis")]
        public string Diagnosis { get; set; }

        [Column("blood_group")]
        public string BloodGroup { get; set; }

    }
}
