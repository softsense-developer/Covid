using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class CompanionGetPatientsResponse:BaseApiResponse
    {
        public class PatientData
        {
            public long Id { get; set; }
            public string Name { get; set; }
            public string Surname { get; set; }
            public string Diagnosis { get; set; }
            public double Oxygen { get; set; }
            public double Pulse { get; set; }
            public double Temperature { get; set; }

            public ValueStatus OxygenWarning { get; set; }
            public ValueStatus PulseWarning { get; set; }
            public ValueStatus TemperatureWarning { get; set; }

            public UserStatus PatientStatus { get; set; }


        }


        public List<PatientData> patientsData { get; set; }


        public CompanionGetPatientsResponse()
        {
            this.patientsData = new List<PatientData>();

        }

    }
}
