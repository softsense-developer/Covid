using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class DoctorGetPatientsResponse:BaseApiResponse
    {
        public class PatientsData { 
        public long Id { get; set; }    
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Diagnosis { get; set; }
        public double Oxygen { get; set; }
        public double Pulse { get; set; }
        public double Temperature { get; set; }
        public string BloodGroup { get; set; }
        public ValueStatus OxygenWarning { get; set; }
        public ValueStatus PulseWarning { get; set; }
        public ValueStatus TemperatureWarning { get; set; }
        public ValueStatus LocationWarning { get; set; }

        public UserStatus PatientStatus { get; set; }

        public string LastDataMinute { get; set; }
        }


        public List<PatientsData> patientsData { get; set; }


        public DoctorGetPatientsResponse()
        {
            this.patientsData = new List<PatientsData>();
            
        }
    }
}
