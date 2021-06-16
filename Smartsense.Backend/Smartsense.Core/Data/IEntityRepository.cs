﻿using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Core.Entities;
using System.Linq.Expressions;
namespace Smartsense.Core.Data
{
    public interface IEntityRepository<T> where T : class, IEntity, new()
    {
        T Get(Expression<Func<T, bool>> filter);

        List<T> GetList(Expression<Func<T, bool>> filter = null);

        T Add(T entity);

        T Update(T entity);

        void Delete(T entity);

    }
}
