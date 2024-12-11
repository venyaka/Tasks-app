//package veniamin.tasksapp.backend.configuration;
//
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.Conditions;
//import org.modelmapper.ModelMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import veniamin.tasksapp.backend.dto.request.task.TaskCreateReqDTO;
//import veniamin.tasksapp.backend.entity.*;
//
//import static org.modelmapper.convention.MatchingStrategies.STANDARD;
//
//@Configuration
//@RequiredArgsConstructor
//public class MapperConfiguration {
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration()
//                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
//                .setSkipNullEnabled(true)
//                .setFieldMatchingEnabled(true)
//                .setDeepCopyEnabled(false)
//                .setMatchingStrategy(STANDARD)
//                .setAmbiguityIgnored(true)
//                .setPropertyCondition(Conditions.isNotNull());
//
//        mapper.createTypeMap(Task.class, TaskCreateReqDTO.class)
//                .addMapping(src -> src.getCreator().getEmail(), TaskCreateReqDTO::setCreatorEmail);
//
//        return mapper;
//    }
//}
