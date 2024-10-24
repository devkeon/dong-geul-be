package com.nemo.dong_geul_be.csv;

import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.clubAndHeadmem.ClubRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;

@Component
@RequiredArgsConstructor
public class CSVDataLoader implements CommandLineRunner {

    private final ClubRepository clubRepository;

    @Override
    public void run(String... args) throws Exception {
        try(Reader reader = new InputStreamReader(new ClassPathResource("csv/club.csv").getInputStream());
            CSVReader csvReader = new CSVReader(reader)){

            String[] line;
            while((line = csvReader.readNext())!=null){
                String clubName = line[0].replace("\uFEFF", "").trim(); // String 그대로 처리
                String studentName = line[1].trim();
                ClubAndHeadMem club = new ClubAndHeadMem();
                club.setClubName(clubName);
                club.setManagerEmail(studentName);
                clubRepository.save(club);
            }
        }
    }
}
