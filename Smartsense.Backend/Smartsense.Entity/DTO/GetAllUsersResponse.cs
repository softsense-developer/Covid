using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Models;

namespace Smartsense.Entity.DTO
{
    public class GetAllUsersResponse : BaseApiResponse
    {
        public List<UsersResponseModel> UsersList { get; set; }

        public GetAllUsersResponse()
        {
            this.UsersList = new List<UsersResponseModel>();
        }
    }
}
