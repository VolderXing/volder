package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Tag;
import generator.service.TagService;
import generator.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Volder
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2025-11-03 15:39:23
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




