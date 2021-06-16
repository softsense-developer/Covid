using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class SupervisorGetDoctorsResponse : BaseApiResponse
    {



        public class DoctorsData { 
        public long Id { get; set; }    
        public string Name { get; set; }
        public string Surname { get; set; }
       
        public List<PatientsData> PatientsDatas { get; set; }
        
        public PatientsData PatientsData { get; set; }
        
        }
        public class PatientsData
        {
            public long Id { get; set; }
            public string Name { get; set; }
            public string Surname { get; set; }
            public string Diagnosis { get; set; }
            public decimal Oxygen { get; set; }
            public decimal Pulse { get; set; }
            public decimal Temperature { get; set; }

            public ValueStatus OxygenWarning { get; set; }
            public ValueStatus PulseWarning { get; set; }
            public ValueStatus TemperatureWarning { get; set; }



        }

        
        public List<DoctorsData> DoctorsDatas { get; set; }


        public SupervisorGetDoctorsResponse()
        {
            this.DoctorsDatas = new List<DoctorsData>();
            
        }
    }
}
