using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Net;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class UserRegisterRequest
    {
        public string Name { get; set; }

        public string Surname { get; set; }
        
        public string Email { get; set; }

        public string Password { get; set; }

        [System.Text.Json.Serialization.JsonIgnore]
        public string Ip { get; set; }

    }
}
