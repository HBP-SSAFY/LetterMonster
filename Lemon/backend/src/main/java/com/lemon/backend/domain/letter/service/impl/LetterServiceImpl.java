package com.lemon.backend.domain.letter.service.impl;

import com.lemon.backend.domain.characters.entity.CharacterMotion;
import com.lemon.backend.domain.characters.entity.Characters;
import com.lemon.backend.domain.characters.repository.CharacterMotionRepository;
import com.lemon.backend.domain.characters.repository.CharacterRepository;
import com.lemon.backend.domain.letter.dto.requestDto.LetterGetListDto;
import com.lemon.backend.domain.letter.dto.responseDto.LetterCreateDto;
import com.lemon.backend.domain.letter.entity.Letter;
import com.lemon.backend.domain.letter.repository.LetterRepository;
import com.lemon.backend.domain.letter.service.LetterService;
import com.lemon.backend.domain.sketchbook.entity.Sketchbook;
import com.lemon.backend.domain.sketchbook.entity.SketchbookCharacterMotion;
import com.lemon.backend.domain.sketchbook.repository.SketchCharacterMotionRepository;
import com.lemon.backend.domain.sketchbook.repository.SketchbookRepository;
import com.lemon.backend.domain.users.user.entity.Users;
import com.lemon.backend.domain.users.user.repository.UserRepository;
import com.lemon.backend.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class LetterServiceImpl implements LetterService {

    private final CharacterRepository characterRepository;
    private final SketchbookRepository sketchbookRepository;
    private final CharacterMotionRepository characterMotionRepository;
    private final SketchCharacterMotionRepository sketchCharacterMotionRepository;
    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    @Override
    public List<LetterGetListDto> getLetterList(Long sketchbookId) {
        return letterRepository.getLetterList(sketchbookId).orElseThrow();
    }

    @Transactional
    @Override
    public Long createLetter(Integer senderId, LetterCreateDto letterDto) {
        SketchbookCharacterMotion sketchbookCharacterMotion = sketchbookRepository.findByCharacterMotionAndSketchbook(letterDto.getSketchbookId(), letterDto.getCharacterMotionId())
                .orElseGet(() -> {
                    Sketchbook sketchbook = sketchbookRepository.findById(letterDto.getSketchbookId()).orElseThrow();
                    CharacterMotion characterMotion = characterMotionRepository.findById(letterDto.getCharacterMotionId()).orElseThrow();
                    SketchbookCharacterMotion newSketchbookCharacterMotion = SketchbookCharacterMotion.builder()
                            .sketchbook(sketchbook)
                            .characterMotion(characterMotion)
                            .build();
                    return sketchCharacterMotionRepository.save(newSketchbookCharacterMotion);
                });

        Users sender = userRepository.findById(senderId).orElseThrow();
        Users receiver = userRepository.findById(sketchbookCharacterMotion.getSketchbook().getUsers().getId()).orElseThrow();

        Letter letter = Letter.builder()
                .sender(sender)
                .receiver(receiver)
                .content(letterDto.getContent())
                .sketchbookCharacterMotion(sketchbookCharacterMotion)
                .build();

        return letterRepository.save(letter).getId();
    }


    @Transactional
    @Override
    public void deleteLetter(Long letterId) {
        letterRepository.deleteById(letterId);
    }
}