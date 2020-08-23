## Treatment of Entities and Tracking

All entities have a unique ID so that they may be tracked. They are housed in managers for easy storage and loading. Alot of entities are associated with an event. this relation shit is stored in the mangers by tracking the id to id of the associated events. Calendars are handled in the same way except they are associated to a user entity ID. The Calendar class acts as a Façade for the the entities. The managers act as use case classes for creation/modification/deletion.



### Data Storage

Loader, AccountAccess and Messaging classes act as gateways to the stored data. The data is stored by serialization. Please note changes made by a user are not saved until they logout. Please also note that all .ser serialization files are currently stored to the phase2/CalendarApp/ directory.



### Controllers and presenters

The AccountAccess acts as a builder for CurrentUser Object which functions as a controller and presenter for application. The access is in turn divided between messaging the calendar functions by the additional controller presenters InboxInterface and CalendarInterface respectively



### UI

All UI functionality is encapsulated in the UI package to ensure that the dependancies run inward to the controllers, presenters, and gateways.



### Timing functionality

An observer observable relationship was created between the CalendarConsole and the CalendarApp to ensure notification when alerts became active. A custom Clock was implemented to allow fastforwarding and reversal of the current time to allow easy demonstration.



### Account Access

Accounts are locked by a password. When the user has been authenticated the UserId is returned which acts as authorization for the rest of the current login session. Only one user can be logged in at a time but this could be changed by allowing multiple CurrentUser. Similarly a user can only have one active calendar at a time, held as a CalendarInterface Interface, but this could be expanded on to allow multiple CalendarInterface instances.



### Messaging

Since Accounts are locked by passwords a sent message cannot be directly placed into another users inbox without authorization. Therefore a MessageManager instance containing undelivered messages (including any shared content) is stored. Upon user Login the MessageManager is queried for any unsent messages to the current user and they are removed from the MessageManager and added to the current user's inbox.