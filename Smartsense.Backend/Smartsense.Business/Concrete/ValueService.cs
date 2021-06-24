using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Mail;
using Smartsense.Data.Abstract;
using Microsoft.EntityFrameworkCore.Query;
using Smartsense.Business.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;
using Smartsense.Entity.Validators;

namespace Smartsense.Business.Concrete
{
    public class ValueService : IValueService
    {
     
        private readonly IValueRepository _valueRepository;
        private readonly IWarningRepository _warningRepository;
        private readonly IUserRepository _userRepository;
        private readonly IPatientRepository _patientRepository;
        public ValueService( IValueRepository valueRepository,IWarningRepository warningRepository ,IUserRepository userRepository ,IPatientRepository patientRepository)
        {
            _warningRepository = warningRepository;
            _valueRepository = valueRepository;
            _userRepository = userRepository;
            _patientRepository = patientRepository;
        }

        public ValueResponse AddValue(ValueRequest request)
        {
            var response = new ValueResponse();

            var validator = new ValueValidator();

            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors)
                {
                    response.Errors.Add(err.ErrorMessage);
                }
                response.Code = "400";
                response.Errors.Add("Doğrulama Hatası");
                return response;
            }



            var value = new Value
         {
             UserId = request.UserId,
             DataType = request.DataType,
             DataValue = request.DataValue,
             Status = true,
             CreatedDate = DateTime.Now,
             SaveDate = DateTime.Now
         };


            var PatientStatus = _patientRepository.Get(p => p.UserId == request.UserId).UserStatus;

            var warning = new Warning();

            if (request.DataType ==DataValueType.OXYGEN && PatientStatus == UserStatus.QUARANTINE_AT_HOME && request.DataValue <=92)
            {
                warning.UserId = request.UserId;
                warning.SaveDate=DateTime.Now;
                warning.WarningData = WarningType.OXYGEN;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }
            if (request.DataType == DataValueType.OXYGEN && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && request.DataValue <= 90)
            {
                warning.UserId = request.UserId;
                warning.SaveDate = DateTime.Now;
                warning.WarningData = WarningType.OXYGEN;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }
            if (request.DataType == DataValueType.TEMPERATURE && PatientStatus == UserStatus.QUARANTINE_AT_HOME && request.DataValue >=38)
            {
                warning.UserId = request.UserId;
                warning.SaveDate = DateTime.Now;
                warning.WarningData = WarningType.TEMPERATURE;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }
            if (request.DataType == DataValueType.TEMPERATURE && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && request.DataValue >= 38.3)
            {
                warning.UserId = request.UserId;
                warning.SaveDate = DateTime.Now;
                warning.WarningData = WarningType.TEMPERATURE;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }
            if (request.DataType == DataValueType.PULSE && PatientStatus == UserStatus.QUARANTINE_AT_HOME && (request.DataValue >= 110 || request.DataValue <=50 ) )
            {
                warning.UserId = request.UserId;
                warning.SaveDate = DateTime.Now;
                warning.WarningData = WarningType.PULSE;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }
            if (request.DataType == DataValueType.PULSE && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && (request.DataValue >= 110 || request.DataValue <= 50))
            {
                warning.UserId = request.UserId;
                warning.SaveDate = DateTime.Now;
                warning.WarningData = WarningType.PULSE;
                warning.WarningValue = (decimal)request.DataValue;
                warning.Status = true;
                warning.CreatedDate = DateTime.Now;
                _warningRepository.Add(warning);
            }

            

            _valueRepository.Add(value);
         //   DummyData();
         response.Code = "200";
         response.Message = "Veri başarıyla eklendi.";
         return response;

        }

        //public void DummyData()
        //{
        //    Random rastgele = new Random();
        //    long id = 92;
        //    DateTime tarih = DateTime.Now.AddDays(-6);
        //    //DateTime tarih = DateTime.Now.AddHours(-10);
        //    DateTime[] dizi = new DateTime[100];
        //    for (int j = 0; j < dizi.Length; j++)
        //    {
        //       // var x = DateTime.Now;
        //         var x = DateTime.Now.AddDays(-6).AddMinutes(j * 9).AddSeconds(j*13);
        //        dizi[j] = x;
        //    }

        //    for (int i = 0; i < 100; i++)
        //    {
        //        tarih.AddMinutes(i * 5);
        //        var value = new Value();
        //        value.UserId = id;
        //        value.DataType = DataValueType.OXYGEN;
        //        value.DataValue = rastgele.Next(95, 99);
        //        value.SaveDate = dizi[i];
        //        value.CreatedDate = dizi[i];

        //        var value2 = new Value();
        //        value2.UserId = id;
        //        value2.DataType = DataValueType.PULSE;
        //        value2.DataValue = rastgele.Next(75, 96);
        //        value2.SaveDate = dizi[i];
        //        value2.CreatedDate = dizi[i];
        //        var value3 = new Value();
        //        value3.UserId = id;
        //        value3.DataType = DataValueType.TEMPERATURE;
        //        value3.DataValue = rastgele.Next(35, 37) + rastgele.NextDouble();
        //        value3.SaveDate = dizi[i];
        //        value3.CreatedDate = dizi[i];

        //        DummyAlert(value, id);
        //        DummyAlert(value2,id);
        //        DummyAlert(value3, id);





        //        _valueRepository.Add(value);
        //        _valueRepository.Add(value2);
        //        _valueRepository.Add(value3);
        //    }


        //}

        //public void DummyAlert(Value request,long id)
        //{
            
        //    var PatientStatus = _patientRepository.Get(p => p.UserId == id).UserStatus;

        //    var warning = new Warning();
        //    if (request.DataType == DataValueType.OXYGEN && PatientStatus == UserStatus.QUARANTINE_AT_HOME && request.DataValue <= 92)
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.OXYGEN;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //    if (request.DataType == DataValueType.OXYGEN && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && request.DataValue <= 90)
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.OXYGEN;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //    if (request.DataType == DataValueType.TEMPERATURE && PatientStatus == UserStatus.QUARANTINE_AT_HOME && request.DataValue >= 38)
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.TEMPERATURE;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //    if (request.DataType == DataValueType.TEMPERATURE && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && request.DataValue >= 38.3)
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.TEMPERATURE;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //    if (request.DataType == DataValueType.PULSE && PatientStatus == UserStatus.QUARANTINE_AT_HOME && (request.DataValue >= 110 || request.DataValue <= 50))
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.PULSE;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //    if (request.DataType == DataValueType.PULSE && PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL && (request.DataValue >= 110 || request.DataValue <= 50))
        //    {
        //        warning.UserId = id;
        //        warning.SaveDate = request.CreatedDate;
        //        warning.WarningData = WarningType.PULSE;
        //        warning.WarningValue = (decimal)request.DataValue;
        //        warning.Status = true;
        //        warning.CreatedDate = request.CreatedDate;
        //        _warningRepository.Add(warning);
        //    }
        //}

        public ValueHistoryResponse GetHistory(ValueHistoryRequest request)
        {
            var response = new ValueHistoryResponse();
            var user = _userRepository.Get(p => p.Id == request.UserId);
            
            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
            }

            var values = _valueRepository.GetList(p => p.UserId == request.UserId);

            if (values == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastaya ait veri bulunamadı");
            }
            
            response.Name = user.Name;
            response.Surname = user.Surname;
            response.Oxygen = values.Where(p => p.DataType == DataValueType.OXYGEN).ToList();
            response.Pulses = values.Where(p => p.DataType == DataValueType.PULSE).ToList();
            response.Temperatures = values.Where(p => p.DataType == DataValueType.TEMPERATURE).ToList();
            
            response.Code = "200";
            response.Message = "Hastaya ait veriler getirildi.";

            return response;

        }

        public GetDateRangeResponse GetHistoryDateRange(GetDateRangeRequest request)
        {
            var response = new GetDateRangeResponse();

            if (request.NewDate.Day - request.OldDate.Day >= 7)
            {
                response.Code = "400";
                response.Errors.Add("Tarih aralığı 7 günden fazla olamaz");
                return response;
            }

            var user = _userRepository.Get(p => p.Id == request.UserId);

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
                return response;
            }

            var values = _valueRepository.GetList(p => p.UserId == request.UserId && p.SaveDate <= request.NewDate && p.SaveDate >= request.OldDate);

            if (values == null)
            {
                response.Code = "200";
                response.Message="Hastaya ait veri bulunamadı";
            }

            response.Name = user.Name;
            response.Surname = user.Surname;
            response.Oxygen = values.Where(p => p.DataType == DataValueType.OXYGEN).ToList();
            response.Oxygen = response.Oxygen.OrderBy(o=>o.SaveDate).ToList();
            response.Pulses = values.Where(p => p.DataType == DataValueType.PULSE).ToList();
            response.Pulses = response.Pulses.OrderBy(o => o.SaveDate).ToList();
            response.Temperatures = values.Where(p => p.DataType == DataValueType.TEMPERATURE).ToList();
            response.Temperatures = response.Temperatures.OrderBy(o => o.SaveDate).ToList();

            response.Code = "200";
            response.Message = "Hastaya ait veriler getirildi.";

            return response;
        }
    }
}