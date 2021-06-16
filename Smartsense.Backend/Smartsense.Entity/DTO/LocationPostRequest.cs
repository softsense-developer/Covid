using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class LocationPostRequest : BaseApiRequest
    {
        public double LatitudeQuarantine { get; set; }
        public double LongitudeQuarantine { get; set; }

        }
}
