﻿using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Core.Entities;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using Smartsense.Core.Data.Ef;

namespace Smartsense.Core.Data.Ef
{
    public class EfQueryableRepository<T> : IQueryableRepository<T> where T : class, IEntity, new()
    {
        private DbContext _context;
        private DbSet<T> _entities;

        public EfQueryableRepository(DbContext context)
        {
            _context = context;
        }

        public IQueryable<T> Table
        {
            get
            {
                return Entities;
            }
        }

        protected virtual DbSet<T> Entities
        {
            get
            {
                if (_entities == null)
                {
                    _entities = _context.Set<T>();
                }
                return _entities;
            }
        }

    }
}
