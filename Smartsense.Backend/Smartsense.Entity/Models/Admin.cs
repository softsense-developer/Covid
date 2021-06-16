using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("admins")]
    public class Admin :BaseEntity
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

        [Column("phone")]
        public string Phone { get; set; }
        
    }
}
