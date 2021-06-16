using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class ConnectionRequestModel
    {
        public long id { get; set; }
        public long UserID { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }

        public bool RequestStatus { get; set; }
        
    }
}
