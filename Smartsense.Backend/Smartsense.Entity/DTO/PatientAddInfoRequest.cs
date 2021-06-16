using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class PatientAddInfoRequest:BaseApiRequest
    {
        //public long DoctorId { get; set; }
        public string IdentityNumber { get; set; }
        public GenderStatus Gender { get; set; }
        public DateTime DateOfBirth { get; set; }
        public UserStatus UserStatus { get; set; }
        public string Diagnosis { get; set; }
        public string BloodGroup { get; set; }
    }
}
