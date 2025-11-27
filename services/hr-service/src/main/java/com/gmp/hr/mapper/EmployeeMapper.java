package com.gmp.hr.mapper;

import com.gmp.hr.dto.EmployeeDTO;
import com.gmp.hr.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 员工实体与DTO映射接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

        EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

        /**
         * 将员工实体转换为DTO
         * 
         * @param employee 员工实体
         * @return 员工DTO
         */
        @Mappings({
                        @Mapping(source = "department.id", target = "departmentId"),
                        @Mapping(source = "department.departmentName", target = "department"),
                        @Mapping(source = "position.id", target = "positionId"),
                        @Mapping(source = "position.positionName", target = "position")
        })
        EmployeeDTO toDTO(Employee employee);

        /**
         * 将员工DTO转换为实体
         * 
         * @param dto 员工DTO
         * @return 员工实体
         */
        @Mappings({
                        @Mapping(source = "dto.idCard", target = "idCard"),
                        @Mapping(source = "dto.phone", target = "phoneNumber"),
                        @Mapping(target = "department", ignore = true),
                        @Mapping(target = "position", ignore = true),
                        @Mapping(target = "qualifications", ignore = true),
                        @Mapping(target = "trainingRecords", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "createdBy", ignore = true),
                        @Mapping(target = "updatedAt", ignore = true),
                        @Mapping(target = "updatedBy", ignore = true),
                        @Mapping(target = "deleted", ignore = true)
        })
        Employee toEntity(EmployeeDTO dto);

        /**
         * 将员工实体列表转换为DTO列表
         * 
         * @param employees 员工实体列表
         * @return 员工DTO列表
         */
        List<EmployeeDTO> toDTOList(List<Employee> employees);

        /**
         * 更新员工实体
         * 
         * @param dto    员工DTO
         * @param entity 员工实体
         * @return 更新后的员工实体
         */
        @Mappings({
                        @Mapping(source = "dto.id", target = "id"),
                        @Mapping(source = "dto.employeeCode", target = "employeeCode"),
                        @Mapping(source = "dto.name", target = "name"),
                        @Mapping(source = "dto.gender", target = "gender"),
                        @Mapping(source = "dto.birthDate", target = "birthDate"),
                        @Mapping(source = "dto.hireDate", target = "hireDate"),
                        @Mapping(source = "dto.departureDate", target = "departureDate"),
                        @Mapping(source = "dto.email", target = "email"),
                        @Mapping(source = "dto.idCard", target = "idCard"),
                        @Mapping(source = "dto.phone", target = "phoneNumber"),
                        @Mapping(source = "dto.status", target = "status"),
                        @Mapping(target = "department", ignore = true),
                        @Mapping(target = "position", ignore = true),
                        @Mapping(target = "qualifications", ignore = true),
                        @Mapping(target = "trainingRecords", ignore = true),
                        @Mapping(target = "createdAt", ignore = true),
                        @Mapping(target = "createdBy", ignore = true),
                        @Mapping(target = "updatedAt", ignore = true),
                        @Mapping(target = "updatedBy", ignore = true),
                        @Mapping(target = "deleted", ignore = true)
        })
        Employee updateEntityFromDTO(EmployeeDTO dto, Employee entity);
}