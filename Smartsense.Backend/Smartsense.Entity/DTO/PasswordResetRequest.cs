using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PasswordResetRequest
    {
        public string Guid { get; set; }
        public string Password { get; set; }
        public string ConfirmPassword { get; set; }
    }
}
