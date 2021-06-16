using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class PatientPutInfoRequest:BaseApiRequest
    {
        
        public DateTime DateOfBirth { get; set; }

        public string IdentityNumber { get; set; }

        public GenderStatus Gender { get; set; }

        public UserStatus UserStatus { get; set; }

        public string Diagnosis { get; set; }
        public string BloodGroup { get; set; }

    }
}
