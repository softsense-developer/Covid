using Smartsense.Entity.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetPatientDataResponse:BaseApiResponse
    {
        public string Name { get; set; }
        public string Surname { get; set; }

        public List<Value> Oxygen { get; set; }

        public List<Value> Pulses { get; set; }

        public List<Value> Temperatures { get; set; }

        public GetPatientDataResponse()
        {
            this.Oxygen = new List<Value>();
            this.Pulses = new List<Value>();
            this.Temperatures = new List<Value>();
        }

    }
}
