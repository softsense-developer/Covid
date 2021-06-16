using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class HastalarInfoModel
    {
        public long HastaId { get; set; }
        public string name { get; set; }
        public string surname { get; set; }
        public string diagnosis { get; set; }
        public double oxygen { get; set; }
        public double pulse { get; set; }
        public double temperature { get; set; }
        public ValueStatus OxygenWarning { get; set; }
        public ValueStatus PulseWarning { get; set; }
        public ValueStatus TemperatureWarning { get; set; }
        public ValueStatus LocationWarning { get; set; }
        public UserStatus PatientStatus { get; set; }


    
}
}
