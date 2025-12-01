package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家属关系实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("family_member")
public class FamilyMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（关联sys_user）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 逝者ID（关联deceased_info）
     */
    @TableField("deceased_id")
    private Long deceasedId;

    /**
     * 家属姓名
     */
    @TableField("member_name")
    private String memberName;

    /**
     * 与逝者关系（如：子女、配偶、父母、兄弟姐妹）
     */
    @TableField("relationship")
    private String relationship;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 联系地址
     */
    @TableField("address")
    private String address;

    /**
     * 是否主要联系人（0-否，1-是）
     */
    @TableField("is_primary")
    private Integer isPrimary;

    /**
     * 是否紧急联系人（0-否，1-是）
     */
    @TableField("emergency_contact")
    private Integer emergencyContact;
}
