using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
   
    public class UserLoginRequest:BaseApiRequest
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public bool isRemember { get; set; }
    }
}
