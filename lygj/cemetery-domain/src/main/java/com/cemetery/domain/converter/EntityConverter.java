package com.cemetery.domain.converter;

import com.cemetery.domain.dto.*;
import com.cemetery.domain.entity.*;
import com.cemetery.domain.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 实体转换器
 */
@Mapper
public interface EntityConverter {

    EntityConverter INSTANCE = Mappers.getMapper(EntityConverter.class);

    // ========== User转换 ==========
    UserVO toUserVO(User user);
    
    List<UserVO> toUserVOList(List<User> userList);
    
    User toUser(UserDTO userDTO);

    // ========== TombLocation转换 ==========
    TombLocationVO toTombLocationVO(TombLocation tombLocation);
    
    List<TombLocationVO> toTombLocationVOList(List<TombLocation> tombLocationList);
    
    TombLocation toTombLocation(TombLocationDTO tombLocationDTO);

    // ========== DeceasedInfo转换 ==========
    DeceasedInfoVO toDeceasedInfoVO(DeceasedInfo deceasedInfo);
    
    List<DeceasedInfoVO> toDeceasedInfoVOList(List<DeceasedInfo> deceasedInfoList);
    
    DeceasedInfo toDeceasedInfo(DeceasedInfoDTO deceasedInfoDTO);

    // ========== ServiceOrder转换 ==========
    ServiceOrderVO toServiceOrderVO(ServiceOrder serviceOrder);
    
    List<ServiceOrderVO> toServiceOrderVOList(List<ServiceOrder> serviceOrderList);
    
    ServiceOrder toServiceOrder(ServiceOrderDTO serviceOrderDTO);

    // ========== PaymentRecord转换 ==========
    PaymentRecordVO toPaymentRecordVO(PaymentRecord paymentRecord);
    
    List<PaymentRecordVO> toPaymentRecordVOList(List<PaymentRecord> paymentRecordList);
    
    PaymentRecord toPaymentRecord(PaymentRecordDTO paymentRecordDTO);

    // ========== ServiceProvider转换 ==========
    ServiceProviderVO toServiceProviderVO(ServiceProvider serviceProvider);
    
    List<ServiceProviderVO> toServiceProviderVOList(List<ServiceProvider> serviceProviderList);
    
    ServiceProvider toServiceProvider(ServiceProviderDTO serviceProviderDTO);

    // ========== ProviderService转换 ==========
    ProviderServiceVO toProviderServiceVO(ProviderService providerService);
    
    List<ProviderServiceVO> toProviderServiceVOList(List<ProviderService> providerServiceList);
    
    ProviderService toProviderService(ProviderServiceDTO providerServiceDTO);

    // ========== ServiceFeedback转换 ==========
    ServiceFeedbackVO toServiceFeedbackVO(ServiceFeedback serviceFeedback);
    
    List<ServiceFeedbackVO> toServiceFeedbackVOList(List<ServiceFeedback> serviceFeedbackList);
    
    ServiceFeedback toServiceFeedback(ServiceFeedbackDTO serviceFeedbackDTO);
}
