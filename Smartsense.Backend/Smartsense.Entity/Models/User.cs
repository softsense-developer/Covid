using System;
using System.Collections.Generic;
using System.Text;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Smartsense.Core.Entities;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.Models
{
    [Table("users")]
    public class User : BaseEntity
    {
        [Column("name")]
        public string Name { get; set; }

        [Column("surname")]
        public string Surname { get; set; }

        [Column("email")]
        public string Email { get; set; }

        [Column("password")]
        public string Password { get; set; }

        [Column("email_confirmed")]
        public bool EmailConfirmed { get; set; }

        [Column("ip")]
        public string Ip { get; set; }
        
        [Column("phone")]
        public string Phone { get; set; }

        [Column("role_id")]
        public long RoleId { get; set; }

        [NotMapped]
        public bool Remember { get; set; }

    }

}
