using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Data.Abstract;
using Smartsense.Core.Data.Ef;
using Smartsense.Entity.Models;

namespace Smartsense.Data.Concrete.Ef
{
    public class EfViewerRepository : EfEntityRepository<Viewer, SmartsenseDbContext>, IViewerRepository { }
}
