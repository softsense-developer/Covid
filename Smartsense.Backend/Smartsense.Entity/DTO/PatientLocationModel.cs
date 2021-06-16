using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientLocationModel
    {
        public long UserId { get; set; }
        
        public string Name { get; set; }

        public string Surname { get; set; }

        public double lat { get; set; }

        public double lng { get; set; }

        public DateTime Time { get; set; }

       
    }
}
