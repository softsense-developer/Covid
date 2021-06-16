using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class UserLoginResponse : BaseApiResponse
    {
        
        public long UserId { get; set; }
        public string Token { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Phone { get; set; }
        public long RoleId { get; set; }
    }
}
