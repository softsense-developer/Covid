using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientGetLocationResponse :BaseApiResponse
    {
        public double LatitudeNow{ get; set; }

        public double LongitudeNow { get; set; }

        public DateTime Time { get; set; }
    }
}
