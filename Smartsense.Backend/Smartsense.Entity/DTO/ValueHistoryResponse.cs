using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class ValueHistoryResponse:BaseApiResponse
    {

        public string Name { get; set; }
        public string Surname { get; set; }

        public List<Value> Oxygen { get; set; }

        public List<Value> Pulses { get; set; }

        public List<Value> Temperatures { get; set; }

        public ValueHistoryResponse()
        {
            
            this.Oxygen = new List<Value>();
            this.Pulses = new List<Value>();
            this.Temperatures = new List<Value>();
        }

    }
}
