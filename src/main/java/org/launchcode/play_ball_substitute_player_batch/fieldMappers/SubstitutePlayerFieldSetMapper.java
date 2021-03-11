package org.launchcode.play_ball_substitute_player_batch.fieldMappers;

import org.launchcode.play_ball_substitute_player_batch.models.SubstitutePlayer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SubstitutePlayerFieldSetMapper implements FieldSetMapper {

    @Override
    public Object mapFieldSet(FieldSet fieldSet) throws BindException {
        return new SubstitutePlayer(
                fieldSet.readString(0),     // gameId
                fieldSet.readInt(1),        // inning
                fieldSet.readInt(2),        // offense 0, defense 1
                fieldSet.readInt(3),        // sequence
                "sub",
                fieldSet.readString(5),     // playerId
                fieldSet.readString(6),     // playerName
                fieldSet.readInt(7),        // visit 0, home1
                fieldSet.readInt(8),        // batting order
                fieldSet.readInt(9),        // field position
                fieldSet.readString(10)    // teamId
        );
    }
}
