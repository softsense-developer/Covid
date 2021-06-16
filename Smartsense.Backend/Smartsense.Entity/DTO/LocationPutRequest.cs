using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class LocationPutRequest :BaseApiRequest
    {
        public double LatitudeNow{ get; set; }
        public double LongitudeNow{ get; set; }
    }
}
