package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.repository.SchoolDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolDeptService {
    private final SchoolDeptRepository schoolDeptRepository;

    public List<SchoolDept> findByIdAndName(Long id, String name) {
        return schoolDeptRepository.findByIdAndName(id, name);
    }
}
