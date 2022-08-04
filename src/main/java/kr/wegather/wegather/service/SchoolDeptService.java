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

    /* SchoolDept 조회 */
    // 복수 조회 - By School
    public List<SchoolDept> findBySchoolAndName(Long schoolId, String name) {
        return schoolDeptRepository.findBySchoolAndName(schoolId, name);
    }
}
