using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class ValueTypeListModel
    {
        public List<ValueWarningModel> Oxygen { get; set; }
        public List<ValueWarningModel> Pulses { get; set; }
        public List<ValueWarningModel> Temperatures { get; set; }
        public List<ValueWarningModel> Locations { get; set; }

        public List<ValueWarningModel> Connections { get; set; }
        public ValueTypeListModel()
        {
            this.Oxygen = new List<ValueWarningModel>();
            this.Pulses = new List<ValueWarningModel>();
            this.Temperatures = new List<ValueWarningModel>();
            this.Locations = new List<ValueWarningModel>();
            this.Connections = new List<ValueWarningModel>();
        }
    }
}
