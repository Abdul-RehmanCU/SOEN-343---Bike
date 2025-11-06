@startuml
title PRC — Combined Activity (Return → Bill → Pay / Export / Dispute)

skinparam ArrowColor #555
skinparam ActivityBackgroundColor #F9FBFF
skinparam ActivityBorderColor #88A

start

partition "Trip Service (Stations/Apps)" {
  :Bike returned at dock;
  :Emit TripCompletion event;
}

partition "PRC System" {
  :Accept TripCompletion event;
  :Fetch trip facts (start/end,\nstations, bikeId, isEbike, riderId);
  :Select PricingPlanVersion (by effectiveDate);

  note right
  Strategy: pick plan version
  CoR: apply pricing rules in order
  (base → per-minute → e-bike surcharge)
  end note

  :Apply pricing rules deterministically;
  :Generate TripSummary;
  :Create immutable LedgerEntry\n(with planVersionId & breakdown);
  :Update Rider pending balance;
  :Send Trip Summary notification;
}

partition "Notification Service" {
  :Deliver trip summary;
}

' ——— Independent post-billing flows (may occur anytime) ———
fork
  ' Rider views billing (optional)
  partition Rider {
    :Open Billing History;
  }
  partition "PRC System" {
    :Retrieve ledger & trip summaries;
    :Render billing items\n(start/end, bike, stations, charges, total, status);
  }
fork again
  ' Rider settles payment (optional)
  partition Rider {
    :Click “Pay now”;
  }
  partition "PRC System" {
    if (Balance > 0?) then (yes)
      :Create PaymentIntent;
    else (no)
      :Show “No payment due”;
      detach
    endif
  }
  partition "External Payment Gateway" {
    :Authorize/confirm payment;
  }
  partition "PRC System" {
    if (Payment confirmed?) then (yes)
      :Mark related ledger entries as PAID;
      :Generate receipt (PDF);
      :Send receipt notification;
    else (no)
      :Show payment failed status;
    endif
  }
  partition "Notification Service" {
    :Deliver receipt / status;
  }
fork again
  ' Admin exports (optional)
  partition Admin {
    :Select export date range;
  }
  partition "PRC System" {
    :Collect matching ledger entries;
    :Generate CSV export;
    :Provide download;
  }
fork again
  ' Dispute flow (optional)
  partition Rider {
    :Submit dispute for a trip;
  }
  partition "PRC System" {
    :Create DisputeTicket;
  }
  partition Admin {
    :Review trip/station logs;
    if (Approved?) then (yes)
      :Approve adjustment;
    else (no)
      :Reject dispute;
    endif
  }
  partition "PRC System" {
    if (Approved?) then (yes)
      :Compute adjustment;
      :Post Adjustment LedgerEntry\n(does not alter original);
      :Send dispute resolution (Approved);
    else (no)
      :Send dispute resolution (Rejected);
    endif
  }
  partition "Notification Service" {
    :Deliver resolution notice;
  }
end fork

stop
@enduml
