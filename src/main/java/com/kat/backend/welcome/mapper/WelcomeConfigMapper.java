package com.kat.backend.welcome.mapper;

import com.kat.backend.welcome.dto.WelcomeConfigDto;
import com.kat.backend.welcome.entity.WelcomeConfig;
import org.springframework.stereotype.Component;

@Component
public class WelcomeConfigMapper {

    public WelcomeConfigDto toDto(WelcomeConfig entity) {
        return WelcomeConfigDto.builder()
                .guildId(entity.getGuildId())
                .welcomeEnabled(entity.isWelcomeEnabled())
                .boostEnabled(entity.isBoostEnabled())
                .welcomeChannelId(entity.getWelcomeChannelId())
                .boostChannelId(entity.getBoostChannelId())
                .welcomeOutputType(entity.getWelcomeOutputType())
                .boostOutputType(entity.getBoostOutputType())
                .messageTemplate(entity.getMessageTemplate())
                .embedTitleTemplate(entity.getEmbedTitleTemplate())
                .embedColor(entity.getEmbedColor())
                .imageTitleTemplate(entity.getImageTitleTemplate())
                .imageUsernameTemplate(entity.getImageUsernameTemplate())
                .imageFooterTemplate(entity.getImageFooterTemplate())
                .imageBackgroundMode(entity.getImageBackgroundMode())
                .imageBackgroundColor(entity.getImageBackgroundColor())
                .imageBackgroundUrl(entity.getImageBackgroundUrl())
                .imageShowAvatar(entity.isImageShowAvatar())
                .imageTextColor(entity.getImageTextColor())
                .imageMentionUser(entity.isImageMentionUser())
                .imageCardEnabled(entity.isImageCardEnabled())
                .imageCardColor(entity.getImageCardColor())
                .imageCardOpacity(entity.getImageCardOpacity())
                .imageAvatarSize(entity.getImageAvatarSize())
                .build();
    }

    public WelcomeConfig toEntity(String guildId, WelcomeConfigDto dto) {
        return WelcomeConfig.builder()
                .guildId(guildId)
                .welcomeEnabled(dto.isWelcomeEnabled())
                .boostEnabled(dto.isBoostEnabled())
                .welcomeChannelId(dto.getWelcomeChannelId())
                .boostChannelId(dto.getBoostChannelId())
                .welcomeOutputType(dto.getWelcomeOutputType())
                .boostOutputType(dto.getBoostOutputType())
                .messageTemplate(dto.getMessageTemplate())
                .embedTitleTemplate(dto.getEmbedTitleTemplate())
                .embedColor(dto.getEmbedColor())
                .imageTitleTemplate(dto.getImageTitleTemplate())
                .imageUsernameTemplate(dto.getImageUsernameTemplate())
                .imageFooterTemplate(dto.getImageFooterTemplate())
                .imageBackgroundMode(dto.getImageBackgroundMode())
                .imageBackgroundColor(dto.getImageBackgroundColor() != null
                        ? dto.getImageBackgroundColor() : "#DCEBFF")
                .imageBackgroundUrl(dto.getImageBackgroundUrl())
                .imageShowAvatar(dto.isImageShowAvatar())
                .imageTextColor(dto.getImageTextColor() != null
                        ? dto.getImageTextColor() : "#0F172A")
                .imageMentionUser(dto.isImageMentionUser())
                .imageCardEnabled(dto.isImageCardEnabled())
                .imageCardColor(dto.getImageCardColor() != null ? dto.getImageCardColor() : "#000000")
                .imageCardOpacity(dto.getImageCardOpacity() > 0 ? dto.getImageCardOpacity() : 120)
                .imageAvatarSize(dto.getImageAvatarSize() > 0 ? dto.getImageAvatarSize() : 140)
                .build();
    }

    public WelcomeConfig defaults(String guildId) {
        return WelcomeConfig.builder()
                .guildId(guildId)
                .welcomeEnabled(false)
                .boostEnabled(false)
                .welcomeOutputType("IMAGE")
                .boostOutputType("IMAGE")
                .messageTemplate("Welcome {userMention} to {server}!")
                .imageTitleTemplate("Welcome to {server}")
                .imageUsernameTemplate("{user}!")
                .imageFooterTemplate("Member #{count}")
                .imageBackgroundMode("COLOR")
                .imageBackgroundColor("#DCEBFF")
                .imageShowAvatar(true)
                .imageTextColor("#0F172A")
                .imageMentionUser(false)
                .imageCardEnabled(true)
                .imageCardColor("#000000")
                .imageCardOpacity(120)
                .imageAvatarSize(140)
                .build();
    }
}