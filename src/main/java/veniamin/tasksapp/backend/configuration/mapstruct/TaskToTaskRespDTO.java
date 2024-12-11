package veniamin.tasksapp.backend.configuration.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskToTaskRespDTO {

    @Mapping(target = "creatorEmail", source = "creator.id")
    @Mapping(target = "performerEmail", source = "performer.id")
    TaskRespDTO sourceToDestination(Task source);
}
