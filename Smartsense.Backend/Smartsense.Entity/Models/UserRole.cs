using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("user_roles")]
    public class UserRole :BaseEntity
    {
        [Column("role_name")]
        public string RoleName { get; set; }

        [Column("user_id")]
        public long UserId { get; set; }
    }
}
