using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Core.Data.Ef;
using Smartsense.Data.Abstract;
using Smartsense.Entity.Models;

namespace Smartsense.Data.Concrete.Ef
{
    public class EfWarningRepository : EfEntityRepository<Warning, SmartsenseDbContext>, IWarningRepository { }
}
