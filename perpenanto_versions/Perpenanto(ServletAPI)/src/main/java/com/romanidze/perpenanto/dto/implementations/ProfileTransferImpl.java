package com.romanidze.perpenanto.dto.implementations;

import com.romanidze.perpenanto.dto.interfaces.ProfileTransferInterface;
import com.romanidze.perpenanto.models.AddressToUser;
import com.romanidze.perpenanto.models.Profile;
import com.romanidze.perpenanto.models.temp.TempProfile;
import com.romanidze.perpenanto.utils.Increase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProfileTransferImpl implements ProfileTransferInterface{

    private Increase mathAction = new Increase();

    @Override
    public List<TempProfile> getTempProfiles(List<Profile> oldList) {

        long count = 0;
        int listSize = oldList.size();
        int countSize = oldList.stream()
                               .mapToInt(profile -> profile.getAddressToUsers().size())
                               .sum();

        List<Long> ids = IntStream.range(0, countSize)
                                  .mapToObj(i -> this.mathAction.incrementLong(count))
                                  .collect(Collectors.toList());

        List<String> personNames = oldList.stream()
                                          .map(Profile::getPersonName)
                                          .collect(Collectors.toList());

        List<String> personSurnames = oldList.stream()
                                             .map(Profile::getPersonSurname)
                                             .collect(Collectors.toList());

        List<Long> userIds =  oldList.stream()
                                     .map(Profile::getUserId)
                                     .collect(Collectors.toList());

        Map<Long, List<AddressToUser>> addressesMap = new HashMap<>();

        for(int i = 0; i < listSize; i++){
            addressesMap.put(userIds.get(i), oldList.get(i).getAddressToUsers());
        }

        List<TempProfile> resultList = IntStream.range(0, countSize)
                                                .mapToObj(i -> new TempProfile(
                                                                                ids.get(i),
                                                                            "",
                                                                          "",
                                                                                  (long) 0,
                                                                                  (long) 0
                                                        )
                                                )
                                                .collect(Collectors.toList());

        int count11 = -1;
        int count12 = -1;
        int count13 = -1;
        int count14 = -1;
        int count1 = -1;
        int count2 = -1;

        addressesMap.forEach((userId, addresses) -> IntStream.range(0, addresses.size()).forEachOrdered(i -> {
            resultList.get(this.mathAction.incrementInt(count11))
                                          .setPersonName(personNames.get(this.mathAction.incrementInt(count1)));
            resultList.get(this.mathAction.incrementInt(count12))
                                          .setPersonSurname(personSurnames.get(this.mathAction.incrementInt(count2)));
            resultList.get(this.mathAction.incrementInt(count13)).setUserId(userId);
            resultList.get(this.mathAction.incrementInt(count14)).setAddressId(addresses.get(i).getId());
        }));

        return resultList;
    }
}
