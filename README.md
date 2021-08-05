![RaidMeter](https://i.imgur.com/nTbYzpH.png)

With **RaidMeter** you can display progress you make in game and trigger something when completed. Very useful with CCI to have a fancy way of displaying its progress.

This mod has the **/raidmeter** command to set everything up:
* /raidmeter add id display_name max_amount current_amount position type
* /raidmeter remove id
* /raidmeter modify id add|set|color|current_amount|max_amount|name|position|type|display_add|display_remove
* /raidmeter info id color|current_amount|max_amount|name|position|type

The add command allows you to create new meters, the remove command allows you to remove a meter, the modify command will allow you to alter any value of the meter. The info command allows you to check the values of the meter. Command autocomplete will help you a lot through the commands, if something doesn't have autocomplete is probably a number.
The `id` value is a short identifier for the meter like `potato2` and `display_name` can be a phrase between quotes like `"Cats are the best"`

![](https://i.imgur.com/vnBHhwR.jpeg)

To do crazy stuff with CCI you can use the Game Hooks feature using the `RaidMeterEvent.Complete` to check when a bar completes more info in this [iChun tutorial](https://www.youtube.com/watch?v=FZKezExrZao). You can also use CCI to trigger the modify command to increase the progress of the meters.

